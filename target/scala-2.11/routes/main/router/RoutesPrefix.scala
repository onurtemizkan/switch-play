
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/onur/Projects/switch/conf/routes
// @DATE:Wed Nov 04 10:08:27 EET 2015


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
