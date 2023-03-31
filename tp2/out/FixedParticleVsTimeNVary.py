import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path
import numpy as np
epsilon = 0.05


if __name__ == '__main__':
    current_index = 0
    # equilibrium_point = [3000, 558, 466, 446, 436, 428, 424, 422]
    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for particle_number in [2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000]:
        regex = 'outputThirdLap_d50_n' + str(particle_number) + '_i0_aggregate.csv'
        files = Path(path).glob(regex)

        upper = []
        lower = []
        for i in range(3000):
            upper.append(0.5 + epsilon)
            lower.append(0.5 - epsilon)

        dfs = [pd.read_csv(f) for f in files]
        df = dfs[0]
        df_ratio = df / particle_number
        df_ratio["left_particle"].plot(label="A la izquierda")
        df_ratio["right_particle"].plot(label="A la derecha")

        plt.xlabel('Número de iteración')
        plt.xlim(0, 3000)
        plt.xticks(np.arange(0, 3000, 300))
        plt.ylabel('Proporción de partículas')
        plt.fill_between([i for i in range(3000)], lower, upper, color='green', alpha=0.2)
        # plt.axvline(x=equilibrium_point[current_index], linestyle='dashed', label='Iteración de equilibrio')
        plt.legend()
        plt.savefig("./plots/FixedThirdTimeRatioN_"+str(particle_number) + '.png')
        plt.clf()
        current_index += 1
