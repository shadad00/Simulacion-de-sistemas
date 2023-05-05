package simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import utils.Pair;

@Getter
@Setter
@AllArgsConstructor
public class ParticleDynamics {
    private Pair r;
    private Pair v;
    private Pair a;
}
