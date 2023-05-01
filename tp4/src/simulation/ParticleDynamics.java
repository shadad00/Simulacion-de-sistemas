package simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.Pair;

@Getter
@Setter
@AllArgsConstructor
public class ParticleDynamics {
    private Pair<Double> r;
    private Pair<Double> v;
    private Pair<Double> a;
}
