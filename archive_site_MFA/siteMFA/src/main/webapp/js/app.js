


document.addEventListener("DOMContentLoaded", function() {
    setTimeout(function() {
        var loader = document.getElementById("loader");
        if (loader) {
            loader.style.top = "-100vh";
        } else {
            console.error("L'élément avec l'ID 'loader' n'a pas été trouvé.");
        }
    }, 1000);

});

