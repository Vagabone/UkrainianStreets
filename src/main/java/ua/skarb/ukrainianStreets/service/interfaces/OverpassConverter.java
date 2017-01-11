package ua.skarb.ukrainianStreets.service.interfaces;

import ua.skarb.ukrainianStreets.domain.OverpassNode;

import java.util.List;

/**
 * Converter from {@code {@link OverpassNode}} to direct class.
 *
 * @author Vynohradov Evgeniy
 * @param <T> direct class.
 */
public interface OverpassConverter<T> {

    /**
     * Convert input array into list of direct class.
     *
     * @param overpassNodes array of {@code {@link OverpassNode}} deserialize from json
     * @return a list of converted values
     */
    List<T> convert(OverpassNode[] overpassNodes);
}
