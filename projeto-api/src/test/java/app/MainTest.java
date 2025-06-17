package app;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    void testHelloEndpoint() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:7000/hello").openConnection();
        con.setRequestMethod("GET");
        assertEquals(200, con.getResponseCode());

        String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        assertEquals("Hello, Javalin!", response);
    }

    @Test
    void testUsuarioCriadoComSucesso() throws IOException {
        URL url = new URL("http://localhost:7000/usuarios");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");

        String jsonInput = "{\"nome\":\"Gui\",\"email\":\"gui@email.com\",\"idade\":25}";
        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonInput.getBytes("utf-8"));
        }

        assertEquals(201, con.getResponseCode());
    }

    @Test
    void testBuscaUsuario() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:7000/usuarios/gui@email.com").openConnection();
        con.setRequestMethod("GET");
        assertEquals(200, con.getResponseCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = reader.readLine();
        assertTrue(response.contains("gui@email.com"));
    }

    @Test
    void testListagemUsuarios() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:7000/usuarios").openConnection();
        con.setRequestMethod("GET");
        assertEquals(200, con.getResponseCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String response = reader.readLine();
        assertTrue(response.startsWith("["));
    }
}