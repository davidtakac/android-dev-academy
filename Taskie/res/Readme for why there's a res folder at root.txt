The ORMLite DatabaseConfigUtil class writes the ormlite_config.txt file to
/res/raw. However, it can't find the ACTUAL location of the /res/raw folder,
which is /app/src/res/raw. This is why I had to make a /res/raw folder at the root
of my project, so the configUtil knows where to save the generated file.

After the file is generated, I just copy it manually to the ACTUAL /app/src/res/raw
folder. 

And yes, I am annoyed by this. 