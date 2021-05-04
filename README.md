# ICCS_PARSER
preprocessor voor vertaling van eigen CSS-dialect (ICCS)

Opdrachtomschrijving
Je gaat in deze opdracht dus een eigen CSS-dialect maken: ICSS-19-SEP(ICA-CSS). Een informele beschrijving van deze taal is te vinden in appendix A. Lees deze beschrijving goed!
Je bouwt in deze opdracht verder aan een interactieve Java-applicatie: de ICSS tool. Deze tool is een interactieve compiler. Je kunt er interactief ICSS in bewerken en deze stapsgewijs compileren naar CSS. Deze CSS kun je vervolgens exporteren. Het raamwerk voor de ICSS tool krijg je als startcode aangeleverd. De GUI is al gemaakt en alle onderdelen zijn in minimale vorm aanwezig. De opdracht bestaat uit het volledig maken van de tool door een serie deelopdrachten.

Voor volledige beschrijving van de opdracht en beoordeling zie assignment.md


# ICSSTool

This file contains notes and issues for the ICSSTool.
For assignment instructions, see [ASSIGNMENT.md](ASSIGNMENT.md)
This tutorial has been tested with Java version 13 (OpenJDK), IntelliJ IDEA and Maven. To enable instructors to assess your work you will need to keep the **Maven POM the same**.

## Running

ICSSTool is a Maven-runnable application.
You can compile the application with the following command:

```sh
mvn compile
```

then run it with either

```sh
mvn exec:java
```

or

```sh
mvn javfx:run
```

Maven will automatically generate/update the parser from the supplied ANTLRv4 grammar file (`.g4`-file).

You can also run the application from an IDE, e.g. IntellIJ. To do so, import [`startcode`](startcode/) as Maven project. 
Whenever you make changes to grammar, make sure you run `mvn generate-sources` prior to compiling. Most IDEs do not update the ANLTR parser automatically.

Since Java is modular, JavaFX is not bundled by default. Depending on your IDE you may need to [install OpenJFX](https://openjfx.io/openjfx-docs/) and add it to your module path.

## Known issues

* Packaging works, but running the JAR standalone can be troublesome because of the JavaFX and ANLTR-runtime dependencies. You can uncomment the `maven-shade-plugin` in the POM to create a (huge) fat JAR. It removes module encapsulation which will trigger a warning.
