document.addEventListener('DOMContentLoaded', () => {
    // Confirmar logout
    const logoutLink = document.getElementById('logoutLink');
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            if (!confirm('¿Seguro que desea cerrar sesión?')) {
                e.preventDefault();
            } else {
                // redirige a /logout
                window.location = '/logout';
            }
        });
    }
    // Validación en formulario de transferencia
    const transferForm = document.querySelector('form[action="/transfer"], form[th\\:action="@{/transfer}"]');
    if (transferForm) {
        transferForm.addEventListener('submit', (e) => {
            const cantidadInput = document.getElementById('cantidad');
            if (cantidadInput) {
                const valor = parseFloat(cantidadInput.value);
                if (isNaN(valor) || valor <= 0) {
                    e.preventDefault();
                    alert('Ingrese una cantidad válida mayor que cero');
                }
            }
        });
    }
});
