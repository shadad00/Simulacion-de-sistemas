import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from pathlib import Path
epsilon = 0.05
particle_equilibrium_map = {}


if __name__ == '__main__':
    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for particle_number in [2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000]:
        particle_interval_upper = (particle_number / 2) + (particle_number * epsilon)
        particle_interval_lower = (particle_number / 2) - (particle_number * epsilon)
        regex = 'outputThirdLap_d50_n' + str(particle_number) + '_i*_aggregate.csv'
        files = Path(path).glob(regex)
        particle_equilibrium_map[particle_number] = []
        for f in files:
            dfs = pd.read_csv(f)
            index = 0
            for m in dfs["left_particle"]:
                if particle_interval_upper >= float(m) >= particle_interval_lower:
                    break
                index += 1
            particle_equilibrium_map[particle_number].append(index)
    average = {}
    for key, value in particle_equilibrium_map.items():
        average[key] = np.mean(value), np.std(value)
    gaps = list(average.keys())
    equilibrium_index = list(average.values())
    plt.errorbar(gaps, [v[0] for v in equilibrium_index], yerr=[[v[1] for v in equilibrium_index]], fmt='o', capsize=5)
    plt.xlabel('Número inicial de particulas')
    plt.ylabel('Número de iteraciones hasta equilibrio')
    plt.yticks(np.arange(0, 3600, 300))
    plt.savefig("VaryingNVsTimeWithErrorBarFirstInEquilibrium.PNG")


