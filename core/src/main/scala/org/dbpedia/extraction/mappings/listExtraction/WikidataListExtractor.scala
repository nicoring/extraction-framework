package org.dbpedia.extraction.mappings

import org.dbpedia.extraction.destinations.{DBpediaDatasets, Dataset, Quad}
import org.dbpedia.extraction.mappings.listExtraction.{TableList, BulletPointList, ListParser}
import org.dbpedia.extraction.ontology.Ontology
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser._

import scala.collection.mutable.ArrayBuffer
import scala.language.postfixOps

/**
 * Extractor to extract Lists from Wikipedia List_of pages
 * adds quads for every list entry
 */
class WikidataListExtractor(
   context: {
     def ontology: Ontology
     def language: Language
   }
) extends PageNodeExtractor with ListParser {

  override val datasets: Set[Dataset] = Set(DBpediaDatasets.WikidataLists)

  // need to be changed to some proper value
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


    val titleText = title.encoded
    val wikiList = parse(pageElems)

    wikiList match {
      case BulletPointList(entries) => {
        println(s"\n\n### $titleText ###\n")
        for (entry <- entries) println(entry.destination.encoded)
      }
      case TableList() => ???
    }

//   createQuads()
    Seq.empty
  }

  /**
   *
   * @param listMembers List of Links in the List
   * @param subjectUri Uri of the list page
   * @param title title of the list Page
   * @return A sequence of quads containing the created triples
   */
  def createQuads(listMembers: List[InternalLinkNode], subjectUri: String, title: WikiTitle): Seq[Quad]= {
    val listIris = for (entry <- listMembers) yield entry.destination.resourceIri

    val quads = new ArrayBuffer[Quad]()

    for (listIri <- listIris) {
      quads += new Quad(context.language, DBpediaDatasets.WikidataLists, listIri, sameAsProperty, subjectUri, title.pageIri)
    }
    quads
  }
}
