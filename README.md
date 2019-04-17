# Simple Java TypeScript Server Bridge

Allows access to the TypeScript tools for compiling TypeScript to JavaScript as well getting completions and errors of TypeScript code directly in Java.

# Using the compiler

Example:

```
FactoryConfig config = FactoryInit.getDefaultConfig();
FactoryInit.init(config);
String nodejsPath = new File("etc/node").getAbsolutePath();
String compilerPath = new File("etc/node_modules/typescript/bin/tsc").getAbsolutePath();
TypeScriptCompiler compiler = new TypeScriptCompiler(nodejsPath, compilerPath);

File tsFile = new File("input.ts");
c_compiler.compile(tsFile);
```

This will compile `input.ts` to `input.js.

# Using the bridge to the TypeScript server

## Init

```
FactoryConfig config = FactoryInit.getDefaultConfig();
FactoryInit.init(config);
String nodejsPath = new File("etc/node").getAbsolutePath();
String tsserverPath = new File("etc/node_modules/typescript/bin/tsserver").getAbsolutePath();
TypeScriptBridge bridge = new TypeScriptBridge(nodejsPath, tsserverPath);
bridge.start();
```

## Get completions

```
TypeScript typeScript = new TypeScript(bridge);
File file = new File("input.ts");
typeScript.openFile(file);
List<String> completions = typeScript.getCompletions(file, 4, 11).get(WAIT_TIME, TimeUnit.SECONDS);
```

Where the first argument is the input file, the second the line number and the last the offset.

## Get signature help

```
File example = new File("example.ts");
int line = 19;
int offset = 18;
TypeScript typeScript = new TypeScript(bridge);
typeScript.openFile(example);
List<String> parameters = typeScript.getParameters(example, line, offset).get(WAIT_TIME, TimeUnit.SECONDS);
```

## Get errors

```
File example = new File("example.ts");
TypeScript typeScript = new TypeScript(bridge);
typeScript.openFile(example);
List<TypeScriptError> errors = typeScript.getErrors(example).get(WAIT_TIME, TimeUnit.SECONDS);
```

## Converting prototype to TypeScript declaration file

```
File custom = new File("custom.js");
String text = FileUtil.readFileIntoString(custom);
JSPrototypeClassToTypeScriptDeclaration converter = new JSPrototypeClassToTypeScriptDeclaration();
Collection<TypeScriptClass> classes = converter.convert(text);
classes.forEach(clazz -> clazz.toString());
```

# Release

## Version 0.1.0

Initial release.
