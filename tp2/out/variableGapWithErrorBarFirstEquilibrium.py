import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from pathlib import Path
epsilon = 0.05
particle_number = 5000
particle_interval_upper = (particle_number / 2) + (particle_number * epsilon)
particle_interval_lower = (particle_number / 2) - (particle_number * epsilon)
consecutive_equilibrium = 500
gap_equilibrium_map = {}


if __name__ == '__main__':
    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for gap in [25, 50, 75, 100, 125, 150, 175, 200]:
        regex = 'outputSecondLap_d' + str(gap) + '_n5000_i*_aggregate.csv'
        files = Path(path).glob(regex)
        gap_equilibrium_map[gap] = []
        for f in files:
            dfs = pd.read_csv(f)
            index = 0
            for m in dfs["left_particle"]:
                if particle_interval_upper >= float(m) >= particle_interval_lower:
                    break
                index += 1
            gap_equilibrium_map[gap].append(index)
    average = {}
    for key, value in gap_equilibrium_map.items():
        average[key] = np.mean(value), np.std(value)
    gaps = list(average.keys())
    equilibrium_index = list(average.values())
    plt.errorbar(gaps, [v[0] for v in equilibrium_index], yerr=[[v[1] for v in equilibrium_index]], fmt='o', capsize=5)
    plt.xlabel('Tamaño del tabique (Cantidad de celdas)')
    plt.ylabel('Numero de iteraciones hasta equilibrio')
    plt.savefig("GapVsTimeWithErrorBarFirstEquilibrium.PNG")


