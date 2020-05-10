JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
        HashTagCounter.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
