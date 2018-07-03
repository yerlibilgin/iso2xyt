# iso2xyt
A simple TOOL that converts an ISO 1979-2 FMR formatted fingerprint to a NIST compatible XYT template.

## Installation

using a terminal, CD to the project dir and run
`mvn verify`
This will create a jar file under target. Copy that jar file to your home directory.

Then edit your .bashrc or .bash_profile and add the following function


```
function iso2xyt(){
   java -jar $HOME/iso2xy2-1.jar Iso2xyt $1 $2
}
```

then restart your terminal or run `. ~/.bashrc` or `. ~/.bash_profile`

## Usage

to convert a fmr file named **foo.txt** to **foo.xyt** do the following

`iso2xyt foo.txt foo.xyt`


Later you can use the xyt file for matching fingerprints using the NIST **bozorth**.


# using as  a library

include maven dependency 

```xml
<dependency>
  <groupId>com.yerlibilgin</groupId>
  <artifactId>iso2xyt</artifactId>
  <version>1</version>
</dependency>
```


And use in your code:

```java
import com.yerlibilgin.biometrics.ISO2XYT;

...

byte []fmrBytes = ... //read bytes from somewhere

byte []xyt = ISO2XYT.iso2xyt(fmrBytes)

```