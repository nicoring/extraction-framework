package org.dbpedia.extraction.mappings.listExtraction

import org.dbpedia.extraction.wikiparser.{InternalLinkNode, TextNode, Node}

/**
 * Extractors for more beautiful pattern matching on the AST of the Page
 */
object Extractors {

  /**
   * Matches one line in a BulletPointList
   */
  object BulletPoint {
    def unapply(node: Node): Boolean = node match {
      case TextNode(text, line) => text contains "*"
      case _ => false
    }
  }

  /**
   * Matches a Link to another Wikipage
   */
  object Link {
    def unapply(node: Node): Option[InternalLinkNode] = node match {
      case node: InternalLinkNode => Some(node)
      case _ => None
    }
  }
}
