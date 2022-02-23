# Frameworks used
<hr/>

### JavaFX

References/Tutorials
<ul>
    <li><a href="https://openjfx.io/javadoc/17/javafx.graphics/javafx/scene/doc-files/cssref.html">CSS docs</a></li>
    <li><a href="https://fxdocs.github.io/docs/html5/#_introduction">JavaFX docs made by someone</a></li>
    <li><a href="https://examples.javacodegeeks.com/desktop-java/javafx/fxml/javafx-fxml-tutorial/">FXML Guide</a></li>
    <li><a href="https://www.youtube.com/watch?v=Z1W4E2d4Yxo">Scene Builder Youtube </a></li>
</ul>

### Juice
<ul>
    <li><a href="https://www.youtube.com/watch?v=wNclLOTxQjk&t=2s">Getting Start Youtube</a></li>
    <li><a href="https://google.github.io/guice/api-docs/4.2/javadoc/index.html?com/google/inject/Binder.html">JavaDoc Juice</a> (not easy to grasp everything)</li>
</ul>


While the project can be run out of the box via Gradle, running it from within Eclipse/Intelij seems to require adding the following as *VM* commands:

    --module-path="path/to/javafx-sdk-17.0.2/lib" --add-modules=javafx.controls,javafx.fxml