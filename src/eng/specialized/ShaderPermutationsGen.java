package eng.specialized;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ShaderPermutationsGen {
    public static void main(String[] args) throws IOException {



        for (int i = 0; i < 100; i++) {

            float k = i / 100f;
            String trig = Math.random() > 0.5 ? "cos" : "sin";

            String fs = String.format("#version 460 core\n" +
                    "\n" +
                    "out vec4 FragColor;\n" +
                    "uniform int u_index;\n" +
                    "\n" +
                    "vec3 MyColorFunc(vec2 uv, float k) {\n" +
                    "\n" +
                    "    float r = 0.5 + 0.5 * %s(uv.x * 10.0 + uv.y * 6.0);\n" +
                    "    float g = 0.5 + 0.5 * %s(uv.x * 10.0 + uv.y * 6.0 + 2.094);\n" +
                    "    float b = 0.5 + 0.5 * %s(uv.x * 10.0 + uv.y * 6.0 + 4.189);\n" +
                    "    return vec3(k * r, k * g, k * b);\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 uv = gl_FragCoord.xy / 800.0;\n" +
                    "\n" +
                    "    FragColor = vec4(MyColorFunc(uv, %f), 1.0);\n" +
                    "}\n", trig, trig, trig, k);



            Path path = Path.of("shaders/naive_specialized/permutations/FragmentShader_" + i + ".glsl");
            Files.writeString(path, fs);

        }

    }
}
