<%-- 
    Document   : Login
    Created on : 10 oct 2024, 19:10:49
    Author     : AlanCeballos
--%>

<%
    //verifica si hay una sesi�n activa
    if (session != null && session.getAttribute("usuario") != null) {
        //redirige a Home.jsp si el usuario ya est� autenticado
        response.sendRedirect("Home.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio de Sesi�n</title>
    <link rel="stylesheet" href="Styles/login.css">
</head>
<body>
    <div class="contenedor-login">
        <h1>Inicio de Sesi�n</h1>
        <form action="login" method="POST"> 
            <label for="usuario">Usuario:</label>
            <input type="text" id="usuario" name="usuario" required>

            <label for="password">Contrase�a:</label>
            <input type="password" id="password" name="password" required>

            <input type="submit" value="Iniciar sesi�n">
        </form>

        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
            <p class="error-message"><%= errorMessage %></p>
        <%
            }
        %>
    </div>
</body>
</html>
