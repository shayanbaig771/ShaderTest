package eng;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public abstract class Benchmark {
    public int numFrames;
    public abstract void init(Window window, ImGuiImplGl3 gl, ImGuiImplGlfw glfw);
    public void launch(String title, int width, int height) {
        Window win = new Window(title, width, height);

        ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
        ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();


        //imgui
        {
            ImGui.createContext();
            imGuiGlfw.init(win.getWindow(), true);
            imGuiGl3.init(win.getGlslVersion());
        }

        glEnable(GL_DEPTH_TEST);
        init(win, imGuiGl3, imGuiGlfw);

        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();
        win.dispose();



    }
}
