
package views.html.helper

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object requireJs_Scope0 {
import play.api.templates.PlayMagic._

class requireJs extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template4[String,String,String,String,play.twirl.api.HtmlFormat.Appendable] {

  /**
 * RequireJS Javascript module loader.
 *
 * Example:
 * {{{
 * @requireJs(core = routes.Assets.at("javascripts/require.js").url, module = routes.Assets.at("javascripts/main").url)
 * }}}
 *
 * @param module Javascript module in question.
 * @param core Reference to require.js.
 * @param productionFolderPrefix Prefix of Javascript production folder, defaut "-min".
 * @param folder Javascript folder, default "javascripts".
 */
  def apply/*15.2*/(module: String, core: String, productionFolderPrefix: String = "-min", folder: String= "javascripts"):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*15.104*/("""


 """),format.raw/*18.2*/("""<script type="text/javascript" data-main=""""),_display_(/*18.45*/{if(play.api.Mode.Prod == play.api.Play.maybeApplication.map(_.mode).getOrElse(play.api.Mode.Dev)) module.replace(folder,folder+productionFolderPrefix) else module}),format.raw/*18.209*/("""" src=""""),_display_(/*18.217*/core),format.raw/*18.221*/(""""></script>
"""))
      }
    }
  }

  def render(module:String,core:String,productionFolderPrefix:String,folder:String): play.twirl.api.HtmlFormat.Appendable = apply(module,core,productionFolderPrefix,folder)

  def f:((String,String,String,String) => play.twirl.api.HtmlFormat.Appendable) = (module,core,productionFolderPrefix,folder) => apply(module,core,productionFolderPrefix,folder)

  def ref: this.type = this

}


}

/**
 * RequireJS Javascript module loader.
 *
 * Example:
 * {{{
 * @requireJs(core = routes.Assets.at("javascripts/require.js").url, module = routes.Assets.at("javascripts/main").url)
 * }}}
 *
 * @param module Javascript module in question.
 * @param core Reference to require.js.
 * @param productionFolderPrefix Prefix of Javascript production folder, defaut "-min".
 * @param folder Javascript folder, default "javascripts".
 */
object requireJs extends requireJs_Scope0.requireJs
              /*
                  -- GENERATED --
                  DATE: Sun Sep 06 18:05:41 PDT 2015
                  SOURCE: /home/play/deploy/playframework/framework/src/play/src/main/scala/views/helper/requireJs.scala.html
                  HASH: e371c33aa78bd2285b75cbdbc9da584d124bba36
                  MATRIX: 868->436|1067->538|1098->542|1168->585|1354->749|1390->757|1416->761
                  LINES: 26->15|31->15|34->18|34->18|34->18|34->18|34->18
                  -- GENERATED --
              */
          