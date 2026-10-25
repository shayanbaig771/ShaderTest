package eng.ubershader;

import eng.*;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.file.Path;

import static org.lwjgl.opengl.GL46.*;

public class Ubershader extends Benchmark {

    public void init(Window win, ImGuiImplGl3 gl, ImGuiImplGlfw glfw) {
        int MESH_COUNT = 100;
        int EXPERIMENT_FRAMES = 100;

        long STARTUP_START_MS = System.currentTimeMillis();
        Mesh[] bunnies = new Mesh[MESH_COUNT];
        Matrix4f model = new Matrix4f();
        float rotation = 1;


        ShaderProgram ubershader = new ShaderProgram(
                FileUtil.readString(Path.of("shaders/ubershader/VertexShader.glsl")),
                FileUtil.readString(Path.of("shaders/ubershader/FragmentShader.glsl"))
        );
        ubershader.prepare();
        ubershader.bind();

        for(int i = 0; i < bunnies.length; i++) {
            Mesh mesh = new Mesh(
                    ubershader,
                    Path.of("models/stanford-bunny.obj")
            );
            mesh.shaderProgram.bind();
            Matrix4f view = new Matrix4f().lookAt(
                    new Vector3f(0.0f, 0.0f, 2.5f),
                    new Vector3f(0.0f, 0.0f, 0.0f),
                    new Vector3f(0.0f, 1.0f, 0.0f)
            );

            mesh.shaderProgram.setMatrix4f("v_view", view);

            Matrix4f projection = new Matrix4f().perspective(
                    (float) Math.toRadians(45.0f),
                    (float) win.getWidth() / win.getHeight(),
                    0.1f,
                    100.0f
            );

            mesh.shaderProgram.setMatrix4f("v_projection", projection);
            bunnies[i] = mesh;
        }
        long[] ACCUM_STARTUP_TIME = new long[]{System.currentTimeMillis() - STARTUP_START_MS};
        long[] ACCUM_SHADER_BIND_TIME = new long[EXPERIMENT_FRAMES * MESH_COUNT];
        long[] ACCUM_DRAW_TIME = new long[EXPERIMENT_FRAMES * MESH_COUNT];
        double[] ACCUM_FRAME_TIME = new double[EXPERIMENT_FRAMES];

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

                    long SHADER_BIND_START_NS = System.nanoTime();
                    mesh.shaderProgram.bind();
                    mesh.shaderProgram.setMatrix4f("v_model", model.identity().translate(px, py, 0).rotate(rotation * i * 0.1f, 0, 1, 0));
                    mesh.shaderProgram.setInt("u_index", i);
                    ACCUM_SHADER_BIND_TIME[numFrames] = (System.nanoTime() - SHADER_BIND_START_NS);

                    long DRAW_START_NS = System.nanoTime();
                    glDrawElements(GL_TRIANGLES, mesh.indices.capacity(), GL_UNSIGNED_INT, 0);
                    ACCUM_DRAW_TIME[numFrames] = (System.nanoTime() - DRAW_START_NS);
                }



                gl.newFrame();
                glfw.newFrame();
                ImGui.newFrame();

                ImGui.showDemoWindow();


                ImGui.render();
                gl.renderDrawData(ImGui.getDrawData());

                ImGui.endFrame();
                win.tick();

                ACCUM_FRAME_TIME[numFrames] = delta;
                numFrames++;

                if(numFrames >= EXPERIMENT_FRAMES) break;
            }
        }

        for(Mesh mesh : bunnies) {
            mesh.shaderProgram.dispose();
            mesh.vbo.dispose();
            mesh.vao.dispose();
        }

        //Write metrics
        {

            FileUtil.writeCSV(Path.of("ubershader_startup_time_ms.csv"), ACCUM_STARTUP_TIME);
            FileUtil.writeCSV(Path.of("ubershader_shader_bind_time_ns.csv"), ACCUM_SHADER_BIND_TIME);
            FileUtil.writeCSV(Path.of("ubershader_draw_time_ns.csv"), ACCUM_DRAW_TIME);
            FileUtil.writeCSV(Path.of("ubershader_frame_time_ms.csv"), ACCUM_FRAME_TIME);

        }
    }

    public static void main(String[] args) {
        new Ubershader().launch("", 1600, 1000);
    }

}