import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path
epsilon = 0.05
consecutive_equilibrium = 100
particle_number_equilibrium_map = {}


if __name__ == '__main__':
    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for particle_number in [2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000]:
        particle_interval_upper = (particle_number / 2) + (particle_number * epsilon)
        particle_interval_lower = (particle_number / 2) - (particle_number * epsilon)
        regex = 'outputThirdLap_d50_n' + str(particle_number) + '_i*_aggregate.csv'
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
        particle_number_equilibrium_map[particle_number] = index
    particle_numbers = list(particle_number_equilibrium_map.keys())
    equilibrium_index = list(particle_number_equilibrium_map.values())
    print(particle_numbers)
    print(equilibrium_index)
    plt.scatter(particle_numbers, equilibrium_index)
    plt.xlabel('Número inicial de particulas')
    plt.ylabel('Número de iteraciones hasta equilibrio')
    plt.savefig("VaryingNVsTime.PNG")


