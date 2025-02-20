function toggleContent(selectedId) {
    // Získame element podľa ID
    const content = document.getElementById(selectedId);

    // Overíme, či je obsah momentálne zobrazený
    if (content.style.display === "none" || content.style.display === "") {
        // Ak je skrytý, zobrazíme ho
        content.style.display = "block";
    } else {
        // Ak je zobrazený, skryjeme ho
        content.style.display = "none";
    }
}

function handleEdit(button) {
    window.location.href = button.getAttribute('data-edit-url');
}