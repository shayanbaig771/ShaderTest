import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL46.*;

public class Main {

    private static ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    public static int WIDTH = 1600, HEIGHT = 1000;

    public static void main(String[] args) throws IOException {

        Window win = new Window("ShaderTest", WIDTH, HEIGHT);

        //imgui
        {
            ImGui.createContext();
            imGuiGlfw.init(win.getWindow(), true);
            imGuiGl3.init(win.getGlslVersion());

        }

        glEnable(GL_DEPTH_TEST);

        Mesh[] bunnies = new Mesh[100];
        for(int i = 0; i < bunnies.length; i++) {
            bunnies[i] = new Mesh(
                    Path.of("shaders/naive_specialized/VertexShader.glsl"),
                    Path.of("shaders/naive_specialized/FragmentShader.glsl"),
                    Path.of("models/stanford-bunny.obj")
            );
        }




        for(Mesh mesh : bunnies) {
            mesh.shaders.bind();
            Matrix4f view = new Matrix4f().lookAt(
                    new Vector3f(0.0f, 0.0f, 2.5f),
                    new Vector3f(0.0f, 0.0f, 0.0f),
                    new Vector3f(0.0f, 1.0f, 0.0f)
            );

            mesh.shaders.setMatrix4f("v_view", view);

            Matrix4f projection = new Matrix4f().perspective(
                    (float) Math.toRadians(45.0f),
                    (float) WIDTH / HEIGHT,
                    0.1f,
                    100.0f
            );

            mesh.shaders.setMatrix4f("v_projection", projection);
        }

        Matrix4f model = new Matrix4f();
        float rotation = 1;





        //loop
        {

            double delta;
            double start = win.getTime();

            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            while (!win.shouldClose()) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                double end = win.getTime();
                delta = end - start;
                start = end;

                int x = 10;
                int y = 10;
                rotation = (float) (rotation + 1f * delta);
                int N = 10; // resolution
                for(int i = 0; i < bunnies.length; i++){

                    int ix = i % N;
                    int iy = i / N;

                    float px = 0.1f * (float) (-x + (2.0 * x) * ix / (N - 1));
                    float py = 0.1f * (float) (-y + (2.0 * y) * iy / (N - 1));


                    Mesh mesh = bunnies[i];
                    mesh.vao.bind();
                    mesh.shaders.bind();

                    mesh.shaders.setMatrix4f("v_model", model.identity().translate(px, py, 0).rotate(rotation * i * 0.1f, 0, 1, 0));
                    glDrawElements(GL_TRIANGLES, mesh.indices.capacity(), GL_UNSIGNED_INT, 0);
                }



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
            for(Mesh mesh : bunnies) {
                mesh.shaders.dispose();
                mesh.vbo.dispose();
                mesh.vao.dispose();
            }
            win.dispose();
        }
    }

}