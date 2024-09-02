# WEB SITE FOR PHONETIC FORCED ALIGNER


## Project organization

Source web files for the web site are in **src/main/webapp**.

Java servlets are in **com/octest/servlets**.

Java beans are is **com/octest/beans**.



## Set up 

This project needs the Montreal Forced Aligner. To download and configure it, you can follow the [MFA documentation](https://montreal-forced-aligner.readthedocs.io/en/latest/getting_started.html).

Once it is done, you need to modify the **MODEL_PATH** and the **DICT_PATH** in Align.java to adapt to the actual location of the MFA on your computer or server.

Please also modify in the WEB-INF/web.xml file the path of the directory **fichierstmp** with the actual location of the project on your computer.

If you want to be able to use the functionality of transcription, you also need to set up Kaldi on your computer. To do so, please follow [this tutorial](https://www.assemblyai.com/blog/kaldi-speech-recognition-for-beginners-a-simple-tutorial/) and clone the given [repository kaldi-asr-tutorial](https://github.com/AssemblyAI/kaldi-asr-tutorial).
**IMPORTANT** : replace the file **main.py** in the cloned directory **kaldi-asr-tutorial/s5** by the **main.py** file in the **src/main/webapp** directory of the project.

Then, you can modify the path of Kaldi in **DataTranscript.java**.

This project can be run by a server like **Apache Tomcat**.  


<br>
 
******************************************************************************
 
 *This code has been realized by Lucie Perrin (lucie.perrin@bordeaux-inp.fr).*
