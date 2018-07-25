# snek

A simple clojure build  of the popular game Snake. Using Java Swing as the GUI. 

## Requirements:

* [Clojure 1.8](http://clojure.org/downloads)
* [Java 8 or higher](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Leiningen](http://leiningen.org/#install)

## Usage

1. `cd` into directory of the repository
2. Execute:

```
 $ lein uberjar
```
3.Run
  * via jar:

     ```
      java -jar snek-0.1.0-standalone.jar [args]
     ```
  * via leiningen:

     ```
      lein run -m app.clj
     ```


## Core Library Used
* **butlast** - returns a sequence of all but the last item in a collection, in linear time.
* **assoc** - when applied to map, returns a new map of the same type that contains mapping of keys to vals.
when applied to vector returns new vector that contains the val at index.
* **cons** - returns a new sequence where x is the first element and sequence is the rest
* **memoize** - returns a memoized version of a referentially transparent function. The memoized version of the funtion
keeps a cache of the mapping from arugments to results and, when calls with the same arugments are repeated has a higher preformence. 
* **with-meta** - adds meta-data
* **count** - returns number of items in collection


## Examples

...

### Bugs

...

## License

 Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.

Distributed under the GNU General Public License either version 3.0 or (at
your option) any later version.
