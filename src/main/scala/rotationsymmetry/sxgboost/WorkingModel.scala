package rotationsymmetry.sxgboost

case class WorkingModel(trees: Array[WorkingNode]) {
  def predict(treePoint: TreePoint): Double = {
    trees.map{ root =>  root.predict(treePoint) }.sum
  }
}