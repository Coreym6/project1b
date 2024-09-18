# Erstelle das bin-Verzeichnis, falls es nicht existiert
build:
	@mkdir -p bin
	@find src -name "*.java" -print | xargs javac -d bin

# Führt das Programm aus und nimmt eine Eingabedatei als Argument
run: build
	@java -cp bin Main $(FILE)

# Bereinigt das Projektverzeichnis
clean:
	@rm -rf bin