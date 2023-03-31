import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path
import numpy as np
epsilon = 0.05
particle_number = 5000


if __name__ == '__main__':
    current_index = 0
    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for gap in [25, 50, 75, 100, 125, 150, 175, 200]:
        regex = 'secondItemVaryingGap_d' + str(gap) + '_n5000_i0_aggregate.csv'
        files = Path(path).glob(regex)

        upper = []
        lower = []
        for i in range(3000):
            upper.append(0.5 + epsilon)
            lower.append(0.5 - epsilon)

        dfs = [pd.read_csv(f) for f in files]
        df = dfs[0]
        total = df["left_particle"] + df["right_particle"]
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
        plt.savefig("./plots/FixedTimeRatioD_"+str(gap) + '.png')
        plt.clf()
        current_index += 1
