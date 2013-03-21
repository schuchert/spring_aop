package shoe.example.schedule;

public interface ConflictResolutionApproach {
  void apply(WorkItem currentHolder, WorkItem contender);
}
