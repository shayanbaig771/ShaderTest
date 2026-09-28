package eng;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public class Mesh {
    ShaderProgram shaders;
    VertexBuffer vbo;
    VertexArray vao;

    IntBuffer indices;
    FloatBuffer vertices;

    public Mesh(
            Path vertexShaderPath,
            Path fragmentShaderPath,
            Path modelPath
    ) throws IOException {

        //GL shenanigans
        {
            shaders = new ShaderProgram(
                    FileUtil.readString(vertexShaderPath),
                    FileUtil.readString(fragmentShaderPath)
            );
            shaders.prepare();
            shaders.bind();

            vbo = new VertexBuffer(
                    100000,
                    100000,
                    3
            );

            {
                InputStream objInputStream = new FileInputStream(modelPath.toFile());
                Obj obj = ObjReader.read(objInputStream);

                // Convert data into simple single-indexed arrays for your buffers
                indices = ObjData.getFaceVertexIndices(obj);
                vertices = ObjData.getVertices(obj);
            }

            vao = new VertexArray();
            vao.setVertexAttributes(
                    new VertexAttribute(0, 3, false, "v_pos")
            );
            vao.bind();
            vao.build();



            glBindBuffer(GL_ARRAY_BUFFER, vbo.myVbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.myEbo);
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, indices);
        }


    }


}
