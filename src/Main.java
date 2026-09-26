import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.*;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL46.*;

public class Main {

    private static ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    public static int WIDTH = 1200, HEIGHT = 800;

    public static void main(String[] args) throws IOException {

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

        IntBuffer indices;
        FloatBuffer vertices;

        //GL shenanigans
        {
            shaders = new ShaderProgram(
                    FileUtil.readString(Path.of("VertexShader.glsl")),
                    FileUtil.readString(Path.of("FragmentShader.glsl"))
            );
            shaders.prepare();
            shaders.bind();

            vbo = new VertexBuffer(
                    100000,
                    100000,
                    3
            );

            {
                InputStream objInputStream = new FileInputStream("stanford-bunny.obj");
                Obj obj = ObjReader.read(objInputStream);

                // Convert data into simple single-indexed arrays for your buffers
                indices = ObjData.getFaceVertexIndices(obj);
                vertices = ObjData.getVertices(obj);
            }

            vao = new VertexArray();
            vao.setVertexAttributes(
                    new VertexAttribute(0, 3, false, "v_pos")
            );
            vao.build();



            glBindBuffer(GL_ARRAY_BUFFER, vbo.myVbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.myEbo);
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);
        }




        {
            Matrix4f view = new Matrix4f().lookAt(
                    new Vector3f(0.0f, 0.3f / 2f, 0.5f / 2f),
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

        Matrix4f model = new Matrix4f();
        float rotation = 1;





        //loop
        {

            double delta;
            double start = win.getTime();

            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            while (!win.shouldClose()) {

                double end = win.getTime();

                delta = end - start;
                start = end;
                //vao.bind();
                shaders.bind();

                rotation = (float) (rotation + 1f * delta);
                shaders.setMatrix4f("v_model", model.identity().translate(0, -0.1f, 0).rotate(rotation, 0, 1, 0));


                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_INT, 0);


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

                    ImGui.separatorText("Metrics");
                    ImGui.text("Frame Rate: " + 1 / delta);
                    ImGui.text("Frame Time: " + delta + "ms");



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