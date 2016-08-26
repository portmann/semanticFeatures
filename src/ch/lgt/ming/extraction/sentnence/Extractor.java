package ch.lgt.ming.extraction.sentnence;

import edu.stanford.nlp.pipeline.Annotation;

public interface Extractor<D>  {
	
	public D extract(Annotation document);

	public int extractCounts(Annotation document);

}
