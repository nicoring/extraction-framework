package org.dbpedia.extraction.mappings

import org.dbpedia.extraction.destinations.{DBpediaDatasets, Quad, Dataset}
import org.dbpedia.extraction.ontology.Ontology
import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser.PageNode

/**
 * Created by nico on 06.05.15.
 */
class WikidataListExtractor(
   config: {
     def ontology: Ontology
     def language: Language
 }) extends Extractor {

  /**
   * @param input The source node
   * @param subjectUri The subject URI of the generated triples
   * @param context The page context which holds the state of the extraction.
   * @return A graph holding the extracted data
   */
  override def extract(input: PageNode, subjectUri: String, context: PageContext): Seq[Quad] = {
    println("\n\n\n here \n\n\n")
    Seq.empty
  }

  override val datasets: Set[Dataset] = Set(DBpediaDatasets.Lists)
}
