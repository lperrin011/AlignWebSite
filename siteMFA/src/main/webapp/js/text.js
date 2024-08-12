

document.addEventListener("DOMContentLoaded", function() {
    fetch('/siteMFA/printText')
        .then(response => response.text())
        .then(data => {
            document.getElementById('textGridContainer').textContent = data;
        })
        .catch(error => {
            document.getElementById('textGridContainer').textContent = 'Erreur de chargement du fichier.';
            console.error('Erreur:', error);
        });
});



/*import {
    Textgrid, IntervalTier, PointTier
  } from '../praatio/lib/textgrid.js';

  import {
    parseTextgrid, serializeTextgrid, serializeTextgridToCsv
  } from '../praatio/lib/textgrid_io.js';

  

  
function loadTextGrid(filePath) {
    fetch(filePath)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}

function printOutDurations(tgFileBuffer, tierName){
    let tg = parseTextgrid(tgFileBuffer);
    let tier = tg.tierDict[tierName];
  
    for (let i = 0; i < tier.entryList.length; i++) {
      let entry = tier.entryList[i];
      console.log(entry[1] - entry[0]);
    }
  };

const filePath = '../text/ALL_050_M_ENG_ENG_HT1.TextGrid';
let fileBuffer = loadTextGrid(filePath);
printOutDurations(fileBuffer, 'words');*/