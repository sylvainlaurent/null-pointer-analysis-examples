# null-pointer-analysis-examples

Demonstrates capabilities of eclipse 4.6 for analysis of (potential) null references.
Just open the java source files in eclipse and look at the comments.

The best analysis is performed using [external annotations](http://help.eclipse.org/neon/topic/org.eclipse.jdt.doc.user/tasks/task-using_external_null_annotations.htm?cp=1_3_9_2).

[This repository/project](https://github.com/sylvainlaurent/eclipse-external-annotations) contains a (non-exhaustive but growing) number of external annotations for usual classes (e.g. Map, List, some guava classes...).

## Inside eclipse IDE
To automatically associate an "annotation path" with the "Maven Dependencies" and "JRE" libraries in eclipse build path:
- install eclipse-external-annotations-m2e-plugin from [this p2 repository](http://sylvainlaurent.github.io/eclipse-external-annotations/p2/).
- add a maven property `m2e.jdt.annotationpath` in your pom, as demonstrated in [with-external-annotations/pom.xml](with-external-annotations/pom.xml).
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

## When running maven from the command-line
To perform null-analysis during a maven build, the jdt compiler must be used in place of the default javac, as demonstrated in the `not-m2e` maven profile of [with-external-annotations/pom.xml](with-external-annotations/pom.xml).

```xml
    <profile>
      <id>not-m2e</id>
      <activation>
        <property>
          <name>!m2e.version</name>
        </property>
      </activation>
      <properties>
        <tycho-version>0.25.0</tycho-version>
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
                      <arg>${project.basedir}/.settings/org.eclipse.jdt.core.prefs</arg>
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
