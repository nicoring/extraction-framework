package org.dbpedia.extraction.mappings

import org.dbpedia.extraction.destinations.{DBpediaDatasets, Dataset, Quad}
import org.dbpedia.extraction.mappings.{PageContext, PageNodeExtractor}
import org.dbpedia.extraction.ontology.Ontology
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser._

import scala.collection.mutable.ArrayBuffer

/**
 * Extractor to extract Lists from Wikipedia List_of pages
 * adds quads for every list entry
 */
class WikidataListExtractor(
   context: {
     def ontology: Ontology
     def language: Language
   }
) extends PageNodeExtractor {

  override val datasets: Set[Dataset] = Set(DBpediaDatasets.WikidataLists)

  // will be changed to some proper value
  private val sameAsProperty = context.ontology.properties("owl:sameAs")

  /**
   * @param page The source node
   * @param subjectUri The subject URI of the generated triples
   * @param pageContext The page context which holds the state of the extraction.
   * @return A graph holding the extracted data
   */
   override def extract(page: PageNode, subjectUri: String, pageContext: PageContext): Seq[Quad] = {
    val pageElems = page.children
    val title = page.title

    // prints the AST
    println(pageElems)

    // list members are all node that are IntenalLinkNodes
    val listMembers = for (InternalLinkNode(destination, children, line, destinationNodes) <- pageElems) yield InternalLinkNode(destination, children, line, destinationNodes)

    val titleText = title.encoded
    println(s"\n\n### $titleText ###\n")

    for (listMember <- listMembers) println(listMember)


    // dummy triple creation
    val listIris = for (entry <- listMembers) yield entry.destination.resourceIri

    val quads = new ArrayBuffer[Quad]()

    for (listIri <- listIris) {
      println(listIri)
      quads += new Quad(context.language, DBpediaDatasets.WikidataLists, listIri, sameAsProperty, subjectUri, title.pageIri)
    }
    quads
  }


  // dont works
//  def getListMembers(pageElems: List[Node]): List[InternalLinkNode] = pageElems match {
//    case Nil => Nil
//    case TextNode("*", 2) :: listNode :: tail => {
//      case listNode: InternalLinkNode => listNode :: getListMembers(tail)
//      case _ => getListMembers(tail)
//    }
//    case _ :: tail => getListMembers(tail)
//  }

}
