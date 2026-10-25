package eng;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public class Mesh {
    public ShaderProgram shaderProgram;
    public VertexBuffer vbo;
    public VertexArray vao;

    public IntBuffer indices;
    FloatBuffer vertices;


    private void init(
            ShaderProgram shaderProgram,
            Path modelPath
    ) {
        this.shaderProgram = shaderProgram;
        vbo = new VertexBuffer(
                100000,
                100000,
                3
        );

        {
            InputStream objInputStream = null;
            Obj obj;
            try {
                objInputStream = new FileInputStream(modelPath.toFile());
                obj = ObjReader.read(objInputStream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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

    public Mesh(
            ShaderProgram shaderProgram,
            Path modelPath
    ) {
        init(shaderProgram, modelPath);
    }
    public Mesh(
            Path vertexShaderPath,
            Path fragmentShaderPath,
            Path modelPath
    ) {

        ShaderProgram shaders = new ShaderProgram(
                FileUtil.readString(vertexShaderPath),
                FileUtil.readString(fragmentShaderPath)
        );
        shaders.prepare();
        shaders.bind();

        init(shaders, modelPath);

    }


}
