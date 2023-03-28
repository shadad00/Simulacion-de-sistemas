import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path
epsilon = 0.05

if __name__ == '__main__':

    path = r'/home/shadad/Desktop/tp2pod/simulacion-de-sistemas/tp2/out'
    for particle_number in [2000, 3000, 5000]:
        regex = '*n' + str(particle_number) + '_i*aggregate.csv'
        files = Path(path).glob(regex)

        upper = []
        lower = []
        for i in range(3000):
            upper.append(0.5 + epsilon)
            lower.append(0.5 - epsilon)

        dfs = [pd.read_csv(f) for f in files]
        df = pd.concat(dfs, ignore_index=True)
        group = df.groupby("iteration")
        media = group.mean()
        std = group.std()
        media_filtered = media[0::100]
        std_filtered = std[::100]
        total_filtered = media_filtered["left_particle"] + media_filtered["right_particle"]
        media_filtered_ratio = media_filtered / particle_number
        std_filtered_ratio = std_filtered / particle_number
        media_filtered_ratio["left_particle"].plot(yerr=std_filtered_ratio["left_particle"], label="A la izquierda")
        media_filtered_ratio["right_particle"].plot(yerr=std_filtered_ratio["right_particle"], label="A la derecha")

        plt.xlabel('Número de iteración')
        plt.ylabel('Proporción de partículas')
        plt.fill_between([i for i in range(3000)], lower, upper, color='green', alpha=0.2)
        plt.legend()
        # plt.title("Proporción de partículas en cada lado respecto del tiempo")
        plt.savefig("./plots/TimeRatioN_"+str(particle_number) + '.png')
        plt.clf()
