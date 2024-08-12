/**
 * 
 */



document.getElementById('import-text').addEventListener('change', function() {
    const hiddenContent = document.querySelector('.hidden-content');
    if (this.checked) {
        hiddenContent.style.display = 'block';
    } else {
        hiddenContent.style.display = 'none';
    }
});


document.getElementById('import-text2').addEventListener('change', function() {
    const hiddenContent = document.querySelector('.hidden-content2');
    if (this.checked) {
        hiddenContent.style.display = 'block';
    } else {
        hiddenContent.style.display = 'none';
    }
});