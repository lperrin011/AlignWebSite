
// window.addEventListener("DOMContentLoaded", (event) => {
//     setTimeout(function(){
//         document.getElementById("loader").style.top = "-100vh";
//     },10000)
// })


document.addEventListener("DOMContentLoaded", function() {
    setTimeout(function() {
        var loader = document.getElementById("loader");
        if (loader) {
            loader.style.top = "-100vh";
        } else {
            console.error("L'élément avec l'ID 'loader' n'a pas été trouvé.");
        }
    }, 1000);


    /*inputs = document.querySelectorAll("input:not(input[type=\"submit\"]), textarea");

    inputs.forEach(e => { parcours le tableau de tous les éléments où on peut renseigner quelque chose
        e.addEventListener("click", function() {  au clic sur l'un des éléments 
            input.forEach(e =>{
                e.style.borderBottom = "2px solid #b4c7dc";  on met tous les éléments de la couleur initiale
            });
            e.style.borderBottom = "2px solid black"  et on met l'élément cliqué en bordure noir 
        }
    )
    })*/
});

