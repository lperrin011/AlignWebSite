function updateProgressBar() {
    fetch('progress')
        .then(response => response.json())
        .then(data => {
            let progressBar = document.getElementById('progress-bar-inner');
            progressBar.style.width = data.progress + '%';
            progressBar.innerHTML = data.progress + '%';
            if (data.progress <= 100) {
                setTimeout(updateProgressBar, 1000); // Continue polling until 100%
            }
        });
}

function startProcessing() {
    const audioFile = document.getElementById('audioFile').value;
    const textFile = document.getElementById('textFile').value;
    const modelPath = document.getElementById('modelPath').value;
    const dictPath = document.getElementById('dictPath').value;

    fetch('progress', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `audioFile=${audioFile}&textFile=${textFile}&modelPath=${modelPath}&dictPath=${dictPath}`
    }).then(() => updateProgressBar());
}
