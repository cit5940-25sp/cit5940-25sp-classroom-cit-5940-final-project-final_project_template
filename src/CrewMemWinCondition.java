import java.util.List;

public class CrewMemWinCondition implements IWinConditionStrategy{
        private String selectedCrewMember;

        public CrewMemWinCondition(String selectedCrewMember) {
            this.selectedCrewMember = selectedCrewMember;
        }

        @Override
        public boolean checkWin(List<IMovie> playedMovies) {
            int count = 0;
            for (IMovie movie : playedMovies) {
                if (movie.getCrew().contains(selectedCrewMember)) {
                    count++;
                }
            }
            return count >= 5;
        }

    @Override
    public String getDescription() {
        return "Play 5 movies of crew member " + selectedCrewMember + ".";
    }

    public String getSelectedCrewMember() {
            return selectedCrewMember;
    }
}

