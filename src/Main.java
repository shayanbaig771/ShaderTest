import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;
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

    private static ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    public static int WIDTH = 1200, HEIGHT = 800;

    public static void main(String[] args) {

        Window win = new Window("ShaderTest", WIDTH, HEIGHT);

        //imgui
        {
            ImGui.createContext();
            imGuiGlfw.init(win.getWindow(), true);
            imGuiGl3.init(win.getGlslVersion());

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
        {
            Matrix4f view = new Matrix4f().lookAt(
                    new Vector3f(0.0f, 0.0f, 10.0f),
                    new Vector3f(0.0f, 0.0f, 0.0f),
                    new Vector3f(0.0f, 1.0f, 0.0f)
            );

            shaders.setMatrix4f("v_view", view);

            Matrix4f projection = new Matrix4f().perspective(
                    (float) Math.toRadians(45.0f),
                    (float) WIDTH / HEIGHT,
                    0.1f,
                    100.0f
            );

            shaders.setMatrix4f("v_projection", projection);
        }







        //loop
        {

            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            while (!win.shouldClose()) {

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
                win.tick();
            }
        }

        //end
        {
            imGuiGlfw.shutdown();
            ImGui.destroyContext();
            shaders.dispose();
            vbo.dispose();
            vao.dispose();
            win.dispose();
        }
    }

}