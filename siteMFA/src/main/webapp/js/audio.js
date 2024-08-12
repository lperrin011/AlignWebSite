var audioTrack = WaveSurfer.create({
    container: '.print-audio',
    waveColor: 'black',
    progressColor: '#b4c7dc',
    barWidth: 2,
  });
  
let audioNameVariable;

 document.addEventListener('DOMContentLoaded', function() {
              fetch('/siteMFA/getAudio')
                  .then(response => response.text())
                  .then(audioName => {
                      // Stocker le résultat dans une variable JavaScript
                      audioNameVariable = audioName.trim();
                      console.log("Audio Name:", audioNameVariable);
	  			
					  
					  /*audioTrack.load('siteMFA/input/' + audioNameVariable);*/
					  audioTrack.load('/siteMFA/serveFile?fileName=' + encodeURIComponent(audioNameVariable));

					  
					  
                      // Utiliser le résultat dans le DOM ou ailleurs
                  /*    document.getElementById('audioName').textContent = audioNameVariable;*/
                  })
                  .catch(error => {
                      console.error('Error:', error);
                  });
          });

/*audioTrack.load('audio/short.wav');*/
const playBtn = document.querySelector(".play-btn");
const stopBtn = document.querySelector(".stop-btn");
const muteBtn = document.querySelector(".mute-btn");
const volumeSlider = document.querySelector(".volume-slider");

playBtn.addEventListener("click", () => {
  audioTrack.playPause();
  if(audioTrack.isPlaying()){
      playBtn.classList.add("playing");
  } else{
      playBtn.classList.remove("playing");
  }
});

stopBtn.addEventListener("click", () =>{
  audioTrack.stop();
  playBtn.classList.remove("playing");
})

volumeSlider.addEventListener("mouseup", () =>{
    changeVolume(volumeSlider.value);
})

volumeSlider.addEventListener("keydown", () =>{
    changeVolume(volumeSlider.value);
})

const changeVolume = (volume) =>{
    if(volume == 0){
        muteBtn.classList.add("muted");
    } 
    else if(muteBtn.classList.contains("muted")){
        muteBtn.classList.remove("muted");
    }
    audioTrack.setVolume(volume);
}



muteBtn.addEventListener("click", () =>{
    if(muteBtn.classList.contains("muted")){
        changeVolume(0.5);
        muteBtn.classList.remove("muted");
        volumeSlider.value = 0.5;
    } else{
        changeVolume(0);  
        muteBtn.classList.add("muted");
        volumeSlider.value = 0;
    }  
})

// Play or pause by pressing the space bar
document.addEventListener('keydown', function(event) {
    if (event.code === 'Space') {
		audioTrack.playPause();
		  if(audioTrack.isPlaying()){
		      playBtn.classList.add("playing");
		  } else{
		      playBtn.classList.remove("playing");
		  }
    }
});


