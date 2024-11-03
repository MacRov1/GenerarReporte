/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {
    const carrito = JSON.parse(localStorage.getItem("carrito")) || [];
    const carritoContainer = document.getElementById("carritoContainer");
    const carritoTotalElement = document.getElementById("carritoTotal");
    const clienteId = localStorage.getItem("clienteId"); // Recuperar el clienteId de localStorage

    // Agregar un console.log para verificar el clienteId
    console.log("Cliente ID:", clienteId);

    // Función para mostrar productos en el carrito
    function mostrarCarrito() {
        carritoContainer.innerHTML = ""; // Limpiar contenido previo

        if (carrito.length === 0) {
            carritoContainer.innerHTML = "<p>No hay productos en el carrito.</p>";
            carritoTotalElement.textContent = "0.00";
            return;
        }

        carrito.forEach((item, index) => {
            const productCard = document.createElement("div");
            productCard.classList.add("col-md-3", "mb-4");

            productCard.innerHTML = `
                <div class="product-card">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">${item.name}</h5>
                            <p class="card-text">Precio Unitario: $${item.price.toFixed(2)}</p>
                            <p class="card-text">Cantidad: ${item.quantity}</p>
                            <p class="card-text">Subtotal: $${(item.price * item.quantity).toFixed(2)}</p>
                            <button class="btn btn-danger btn-remove" data-index="${index}">Quitar del Carrito</button>
                        </div>
                    </div>
                </div>
            `;
            carritoContainer.appendChild(productCard);
        });

        actualizarTotal();
    }

    // Función para actualizar el total del carrito
    function actualizarTotal() {
        const total = carrito.reduce((sum, item) => sum + item.price * item.quantity, 0);
        carritoTotalElement.textContent = total.toFixed(2);
    }

    // Evento para quitar productos del carrito
    carritoContainer.addEventListener("click", function (event) {
        if (event.target.classList.contains("btn-remove")) {
            const index = event.target.getAttribute("data-index");
            carrito.splice(index, 1);  // Eliminar el producto del carrito
            localStorage.setItem("carrito", JSON.stringify(carrito));  // Actualizar localStorage
            mostrarCarrito();  // Volver a mostrar el carrito
        }
    });

    // Evento para confirmar el pedido
    document.getElementById("confirmarPedido").addEventListener("click", function () {
        if (carrito.length === 0) {
            alert("El carrito está vacío. Añada productos antes de confirmar.");
            return;
        }

        confirmarPedido(clienteId, carrito); // Llama a la función para confirmar el pedido
    });

    // Definición de la función confirmarPedido
    function confirmarPedido(clienteId, carrito) {
        console.log("Confirmando pedido con Cliente ID:", clienteId); // Agregado para depuración

        
        // Verificar si clienteId es nulo o indefinido
        if (!clienteId) {
            alert("Cliente ID no está disponible. Por favor, seleccione un cliente.");
            return; // Salir de la función si el clienteId no es válido
        }

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "verCarrito?confirmarPedido=true", true);

        // Cambiar el tipo de contenido a JSON
        xhr.setRequestHeader("Content-Type", "application/json");

        // Prepara el cuerpo de la solicitud
        const body = JSON.stringify({clienteId: clienteId, carrito: carrito});

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                alert(xhr.responseText); // Muestra el mensaje de confirmación o error
                // Limpia el carrito en localStorage y actualiza la vista
                localStorage.removeItem("carrito");
                mostrarCarrito();
                window.location.href = "crearPedido"; // Redirige a la página de creación de pedido
            }
        };

        xhr.send(body); // Enviar la solicitud con el cuerpo
    }

    // Evento para limpiar el carrito
    document.getElementById("limpiarCarrito").addEventListener("click", function () {
        localStorage.removeItem("carrito");
        carrito.length = 0;  // Vaciar el carrito en memoria
        mostrarCarrito();
    });

    // Mostrar el carrito al cargar la página
    mostrarCarrito();
});