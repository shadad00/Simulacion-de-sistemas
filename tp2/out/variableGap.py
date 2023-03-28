import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path
epsilon = 0.05
particle_number = 5000
particle_interval_upper = (particle_number / 2) + (particle_number * epsilon)
particle_interval_lower = (particle_number / 2) - (particle_number * epsilon)
consecutive_equilibrium = 100
gap_equilibrium_map = {}


if __name__ == '__main__':
    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for gap in [25, 50, 75, 100, 125, 150, 175, 200]:
        regex = 'outputSecondLap_d' + str(gap) + '_n5000_i*_aggregate.csv'
        files = Path(path).glob(regex)
        dfs = [pd.read_csv(f) for f in files]
        df = pd.concat(dfs, ignore_index=True)
        group = df.groupby("iteration")
        media = group.mean()
        std = group.std()
        counter = 0
        index = 0
        for m, s in zip(media["left_particle"], std["left_particle"]):
            if particle_interval_upper >= float(m) + float(s) and float(m) - float(s) >= particle_interval_lower:
                counter = counter + 1
            else:
                counter = 0
            if counter == consecutive_equilibrium:
                index -= consecutive_equilibrium
                break
            index += 1
        gap_equilibrium_map[gap] = index
    gaps = list(gap_equilibrium_map.keys())
    equilibrium_index = list(gap_equilibrium_map.values())
    print(gaps)
    print(equilibrium_index)
    plt.scatter(gaps, equilibrium_index)
    plt.xlabel('Tamaño del tabique (Cantidad de celdas)')
    plt.ylabel('Numero de iteraciones hasta equilibrio')
    plt.savefig("GapVsTime.PNG")


