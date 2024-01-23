= Ideas for Performance Changes

* DomNode: Use Copy on Write for listeners to safe on object holders, but CopyOnWriteArrayList is not good enough
* DomNode: Remove synchronized and find something better without extra cost for more objects, maybe a ReentrantLock at the SGMLPage

