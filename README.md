# null-pointer-analysis-examples

Demonstrates capabilities of some java tools to analyze (potential) null references. These tools are:
- [eclipse (4.7)](https://www.eclipse.org)
- [IntelliJ (2017.2)](https://www.jetbrains.com/idea/)
- [The Checker Framework](https://checkerframework.org/)

Open the various java sources and read the comments to have an understanding of various checks that can be done (or not)
by these tools.

:warning: It is NORMAL that your IDE shows errors in the source files if it supports some checks for null reference analysis.:warning:
The default javac compiler does not report any error however.

## Comparison

Feature | Eclipse IDE 4.7.1 or jdt | Checker Framework | IntelliJ 2017.2.5
------- | ------------------ | ----------------- | --------
IDE support | :white_check_mark: | :white_check_mark: using plugin | :white_check_mark:
Command line support | :white_check_mark: | :white_check_mark: | To be tested
Java 8 type annotations support | :white_check_mark: | :white_check_mark: | :white_check_mark:
Multiple annotations classes supported | :white_check_mark: | :white_check_mark: | :white_check_mark:
External annotations support | :white_check_mark: using .eea files | :white_check_mark: using stubs files | :white_check_mark: using xml files
External annotations provided for common libraries | :red_circle: community effort with [lastnpe.org](http://lastnpe.org)| :white_check_mark: JDK, Guava | :white_check_mark: JDK
IDE support to create external annotations | :white_check_mark: | :warning: using command line tools | :white_check_mark:
Treat all types as @Nonnull by default, unless annotated wih @Nullable | :white_check_mark: using @NonNullByDefault for each package, allows to define the scope: field, parameters, return, generic types, etc... | :white_check_mark: by default, customizable with @DefaultQualifier | :white_check_mark: 
@Polynull support | :red_circle: | :white_check_mark: using [@PolyNull](https://checkerframework.org/manual/#qualifier-polymorphism) | :white_check_mark: using [@Contract](https://www.jetbrains.com/help/idea/2017.1/contract-annotations.html), for instance @Contract("!null->!null;null->null")
Method contract support (e.g. handle ``if(StringUtils.hasText(str)) {str...}`` | :red_circle: | :question: | :white_check_mark: using @Contract
Automatic inference of nullability constraints in external libraries | :red_circle: | :red_circle: | :white_check_mark: for @NonNull and some @Contract. [Disabled for @Nullable due to too many false positives](https://youtrack.jetbrains.com/issue/IDEA-130063)
Treat main, test or generated sources differently | :warning: not in IDE, unless ignoring all non-fatal errors for a source folder| :white_check_mark: | :white_check_mark:

## Eclipse
Last version tested: eclipse 4.7.1

The best analysis is performed using [external annotations](http://help.eclipse.org/neon/topic/org.eclipse.jdt.doc.user/tasks/task-using_external_null_annotations.htm?cp=1_3_9_2).

[This repository/project](https://github.com/sylvainlaurent/eclipse-external-annotations) contains a (non-exhaustive but growing) number of external annotations for usual classes (e.g. Map, List, some guava classes...).

### Inside eclipse IDE

To activate the null reference analysis in this example project, copy ``ide-settings/eclipse-no-npe-analysis/org.eclipse.jdt.core.prefs`` to the ``.settings/`` directory.

To automatically associate an "annotation path" with the "Maven Dependencies" and "JRE" libraries in eclipse build path:
- install eclipse-external-annotations-m2e-plugin from [this p2 repository](http://sylvainlaurent.github.io/eclipse-external-annotations/p2/).
- add a maven property `m2e.jdt.annotationpath` in your pom, as demonstrated in [pom.xml](pom.xml#93).
- perform a full `Maven/Update project...` in eclipse.

Tip: place the property in a `m2e` profile activated only inside eclipse, not in the command-line (see below for command-line usage).
Using a source project for external annotations in the same eclipse workspace allows to quickly [add missing annotations directly from eclipse](http://help.eclipse.org/neon/topic/org.eclipse.jdt.doc.user/tasks/task-using_external_null_annotations.htm?cp=1_3_9_2_2#create).

```xml
    <profile>
      <id>m2e</id>
      <activation>
        <property>
          <name>m2e.version</name>
        </property>
      </activation>
      <properties>
        <!-- the following is effective if the eclipse-external-annotations-m2e-plugin is installed and the eclipse-external-annotations project is open in the same workspace -->
        <m2e.jdt.annotationpath>/eclipse-external-annotations/src/main/resources</m2e.jdt.annotationpath>
      </properties>
    </profile>
```

### Using eclipse jdt compiler with maven from the command-line
To perform the same null reference analysis as the eclipse IDE during a maven build, the jdt compiler must be used in place of the default javac, as demonstrated in the `jdt` maven profile of [pom.xml](pom.xml).

```xml
    <profile>
      <id>jdt</id>
      <properties>
        <tycho-version>0.26.0</tycho-version>
      </properties>
      <repositories>
        <repository>
          <!-- just to retrieve snapshots of com.github.sylvainlaurent:null-pointer-analysis-examples. Useless if using versions released to maven central -->
          <id>ossrh-snapshots</id>
          <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
          <snapshots>
            <enabled>true</enabled>
            <checksumPolicy>warn</checksumPolicy>
          </snapshots>
        </repository>
      </repositories>
      <dependencies>
        <dependency>
          <groupId>com.github.sylvainlaurent</groupId>
          <artifactId>eclipse-external-annotations</artifactId>
          <version>0.0.1-SNAPSHOT</version>
          <scope>provided</scope>
        </dependency>
      </dependencies>
      <build>
        <pluginManagement>
          <plugins>
            <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-compiler-plugin</artifactId>
              <executions>
                <execution>
                  <!-- check nullability using jdt compiler, only for compile, not for testCompile -->
                  <id>compile</id>
                  <phase>compile</phase>
                  <goals>
                    <goal>compile</goal>
                  </goals>
                  <configuration>
                    <compilerId>jdt</compilerId>
                    <compilerArgs>
                      <arg>-properties</arg>
                      <arg>${project.basedir}/ide-settings/eclipse-with-npe-analysis/org.eclipse.jdt.core.prefs</arg>
                      <arg>-annotationpath</arg>
                      <arg>CLASSPATH</arg>
                    </compilerArgs>
                  </configuration>
                </execution>
              </executions>
              <dependencies>
                <dependency>
                  <groupId>org.eclipse.tycho</groupId>
                  <artifactId>tycho-compiler-jdt</artifactId>
                  <version>${tycho-version}</version>
                </dependency>
              </dependencies>
            </plugin>
          </plugins>
        </pluginManagement>
      </build>
    </profile>
```

To launch the compilation with this jdt profile, run: ``mvn clean test -P jdt``. The compiler should find the same errors as in eclipse:
```
$ mvn clean test -P jdt
...
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/packageNonNull/ClassInAnnotatedPackage.java:[13] 
    echo(null);
         ^^^^
Null type mismatch: required '@NonNull String' but the provided value is null
[ERROR] /Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/test/EverythingNonNullByDefault.java:[25] 
    public EverythingNonNullByDefault(String name) {
           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The @NonNull field address may not have been initialized
[ERROR] /Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/test/EverythingNonNullByDefault.java:[47] 
    return city;
           ^^^^
...
```

## IntelliJ
WIP

## Checker Framework
Though the Checker Framework proposes many checks, we only consider its [Nullness checker](https://checkerframework.org/manual/#nullness-checker) for the purpose of this example project.

In this example project, the ``checker-framework`` maven profile allows to use this nullness checker:

```xml
    <profile>
      <id>checker-framework</id>
      <dependencies>
        <dependency>
          <groupId>org.checkerframework</groupId>
          <artifactId>checker</artifactId>
          <version>${checker-framework.version}</version>
        </dependency>
        <dependency>
          <groupId>org.checkerframework</groupId>
          <artifactId>jdk8</artifactId>
          <version>${checker-framework.version}</version>
        </dependency>
      </dependencies>
      <build>
        <plugins>
          <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
              <fork>true</fork>
              <annotationProcessors>
                <!-- Add all the checkers you want to enable here -->
                <annotationProcessor>org.checkerframework.checker.nullness.NullnessChecker</annotationProcessor>
              </annotationProcessors>
              <compilerArgs>
                <!-- location of the annotated JDK, which comes from a Maven dependency -->
                <arg>-Xbootclasspath/p:${org.checkerframework:jdk8:jar}</arg>
              </compilerArgs>
            </configuration>
          </plugin>
          <plugin>
            <!-- This plugin will set properties values using dependency information -->
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <version>2.3</version>
            <executions>
              <execution>
                <goals>
                  <goal>properties</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
```

To launch the compilation with this profile, run: ``mvn clean test -P checker-framework``. The compiler should find errors:
```
$ mvn clean test -P checker-framework
...
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/packageNotAnnotated/ClassInNotAnnotatedPackage.java:[10,13] error: [argument.type.incompatible] incompatible types in argument.
[ERROR]   found   : null
  required: @Initialized @NonNull String
/Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/packageNotAnnotated/ClassInNotAnnotatedPackage.java:[16,19] error: [return.type.incompatible] incompatible types in return.
[ERROR]   found   : null
  required: @Initialized @NonNull String
/Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/packageNonNull/ClassInAnnotatedPackage.java:[13,7] error: [argument.type.incompatible] incompatible types in argument.
[ERROR]   found   : null
  required: @Initialized @NonNull String
/Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/packageNonNull/ClassInAnnotatedPackage.java:[19,10] error: [return.type.incompatible] incompatible types in return.
[ERROR]   found   : null
  required: @Initialized @NonNull String
/Users/slaurent/Developer/repos/null-pointer-analysis-examples/src/main/java/test/EverythingNonNullByDefault.java:[25,11] error: [initialization.fields.uninitialized] the constructor does not initialize fields: address
...
```
