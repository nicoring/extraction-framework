package org.dbpedia.extraction.mappings.listExtraction

import org.dbpedia.extraction.mappings.listExtraction.Extractors._
import org.dbpedia.extraction.wikiparser.{InternalLinkNode, Node}


/**
 * The different Types of Lists
 */
trait WikiList
case class BulletPointList(entries: List[InternalLinkNode]) extends WikiList
case class TableList() extends WikiList

trait ListParser {

  /**
   * This method should be called to extract the list.
   * It determines the type of the list and parses it accordingly.
   *
   * @param page The elements of the Wikipage which contains the list
   * @return A Wikipage Object holding a list of entries
   */
  def parse(page: List[Node]): WikiList = {
    val listType = getTypeOf(page)
    listType match {
      case BulletPointList(Nil) => getListEntries(page)
      case TableList() => getTableEntries(page)
    }
  }

  /**
   * Should determine the type of the given page
   *
   * @param page
   * @return
   */
  def getTypeOf(page: List[Node]): WikiList = BulletPointList(Nil)

  /**
   * Gets the list entries of a BulletPointList.
   * The entries must have this format: `* [[WikiLink]]`
   *
   * @param page elements of the wikipage
   * @return a BulletPointList object containing the list entries
   */
  def getListEntries(page: List[Node]): BulletPointList = {
    def iterHelper(page: List[Node], entries: List[InternalLinkNode]): BulletPointList = page match {
      case Nil => BulletPointList(entries)
      case BulletPoint() :: Link(link) :: tail => iterHelper(tail, link :: entries)
      case head :: tail => iterHelper(tail, entries)
    }
    iterHelper(page, Nil)
  }

  /**
   * Gets the list entries of a TableList.
   *
   * @param page
   * @return
   */
  def getTableEntries(page: List[Node]): TableList = ???
}


