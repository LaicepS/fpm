package com.mappy.fpm.batches.tomtom.dbf.lanes;

import com.mappy.fpm.batches.utils.Feature;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.reverse;
import static com.google.common.collect.Maps.newHashMap;
import static com.mappy.fpm.batches.tomtom.helpers.RoadTagger.isReversed;

public class LaneTagger {

    private final LdDbf ldDbf;

    @Inject
    public LaneTagger(LdDbf ldDbf) {
        this.ldDbf = ldDbf;
    }

    public Map<String, String> lanesFor(Feature feature) {
        Map<String, String> tags = newHashMap();

        if (ldDbf.containsKey(feature.getLong("ID"))) {
            tags.put("turn:lanes", on("|").join(reorder(feature)));
        }

        Integer lanes = feature.getInteger("LANES");
        if (lanes > 0) {
            tags.put("lanes", String.valueOf(lanes));
        }

        return tags;
    }

    private List<String> reorder(Feature feature) {
        List<String> parts = ldDbf.get(feature.getLong("ID"));
        if (isReversed(feature)) {
            return parts;
        }
        return reverse(parts);
    }
}
