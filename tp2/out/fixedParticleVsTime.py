import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path
epsilon = 0.05

if __name__ == '__main__':

    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for particle_number in [2000, 3000, 5000]:
        regex = '*output_d50_n' + str(particle_number) + '_i1_aggregate.csv'
        files = Path(path).glob(regex)
        upper = []
        lower = []
        for i in range(3000):
            upper.append(0.5 + epsilon)
            lower.append(0.5 - epsilon)

        dfs = [pd.read_csv(f) for f in files]
        data_frame = dfs[0]
        total_particle = data_frame["left_particle"] + data_frame["right_particle"]
        left_particle_ratio = data_frame["left_particle"] / particle_number
        right_particle_ratio = data_frame["right_particle"] / particle_number
        left_particle_ratio.plot(label="A la izquierda")
        right_particle_ratio.plot(label="A la derecha")
        plt.xlabel('Número de iteración')
        plt.ylabel('Proporción de partículas')
        plt.fill_between([i for i in range(3000)], lower, upper, color='green', alpha=0.2)
        plt.legend()
        # plt.title("Proporción de partículas en cada lado respecto del tiempo")
        plt.savefig("./plots/FixedTimeRatioN_"+str(particle_number) + '.png')
        plt.clf()
