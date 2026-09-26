import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.nio.file.Path;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private static long window;
    private static ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static String glslVersion = null;

    public static void main(String[] args) {
        //init
        {
            GLFWErrorCallback.createPrint(System.err).set();
            if ( !glfwInit() )
                throw new IllegalStateException("Unable to initialize GLFW");

            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
            {
                final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
                if (isMac) {
                    glslVersion = "#version 150";
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
                    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);  // 3.2+ only
                    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);          // Required on Mac
                } else {
                    glslVersion = "#version 130";
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
                }
            }

            window = glfwCreateWindow(1200, 800, "ShaderTest", NULL, NULL);
            if ( window == NULL )
                throw new RuntimeException("Failed to create the GLFW window");



            try ( MemoryStack stack = stackPush() ) {
                IntBuffer pWidth = stack.mallocInt(1); // int*
                IntBuffer pHeight = stack.mallocInt(1); // int*

                glfwGetWindowSize(window, pWidth, pHeight);

                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());


                glfwSetWindowPos(
                        window,
                        (vidmode.width() - pWidth.get(0)) / 2,
                        (vidmode.height() - pHeight.get(0)) / 2
                );
            }
            glfwMakeContextCurrent(window);
            GL.createCapabilities();
            glfwSwapInterval(1);
            glfwShowWindow(window);


        }

        //imgui
        {
            ImGui.createContext();
            imGuiGlfw.init(window, true);
            imGuiGl3.init(glslVersion);

        }


        ShaderProgram shaders;
        VertexBuffer vbo;
        VertexArray vao;

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        float[] vertices = {
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f
        };


        //GL shenanigans
        {
            shaders = new ShaderProgram(
                    FileUtil.readString(Path.of("VertexShader.glsl")),
                    FileUtil.readString(Path.of("FragmentShader.glsl"))
            );
            shaders.prepare();
            shaders.bind();

            vbo = new VertexBuffer(
                    4,
                    Float.BYTES * 2
            );

            vao = new VertexArray();
            vao.setVertexAttributes(
                    new VertexAttribute(0, 2, false, "v_pos")
            );
            vao.build();


            glBindBuffer(GL_ARRAY_BUFFER, vbo.myVbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.myEbo);
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);
        }


        shaders.setMatrix4f("v_model", new Matrix4f());
        shaders.setMatrix4f("v_view", new Matrix4f());
        shaders.setMatrix4f("v_projection", new Matrix4f());







        //loop
        {

            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            while ( !glfwWindowShouldClose(window) ) {

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);


                imGuiGl3.newFrame();
                imGuiGlfw.newFrame();
                ImGui.newFrame();

                ImGui.begin("uOttawa ENG1112 Research Project Demo");
                {
                    ImGui.separatorText("A project by:");
                    ImGui.text("Baig, Mohammed\n" +
                            "Derk, Justin\n" +
                            "Obeng Asante, Princess\n" +
                            "Rahal, Batoul\n");

                    ImGui.separatorText("Tests and Metrics");


                }
                ImGui.end();


                ImGui.render();
                imGuiGl3.renderDrawData(ImGui.getDrawData());

                ImGui.endFrame();


                glfwSwapBuffers(window);
                glfwPollEvents();
            }
        }

        //end
        {
            imGuiGlfw.shutdown();
            ImGui.destroyContext();


            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

}