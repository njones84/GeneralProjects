using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMovement : MonoBehaviour
{

    [SerializeField] private Transform trans;
    [SerializeField] private Rigidbody rb;

    private float movementSpeed;
    private float radiusOfSat;
    private float obstacleDodgeSpeed;
    private bool arrived;

    private Vector3 obstaclePosition;
    private Vector3 targetPosition;
    private Quaternion targetRotation;
    private Pathfinder pathfinder;
    private Stack<Vector3> path;

    void Start()
    {
        pathfinder = gameObject.GetComponent<Pathfinder>();
        path = new Stack<Vector3>();

        movementSpeed = 5f;
        radiusOfSat = 2f;
        obstacleDodgeSpeed = 1f;
        arrived = true;
    }

    void Update()
    {
        if (Input.GetMouseButtonDown(0))
            GetWorldPoint();

        if (Input.GetMouseButtonDown(1))
            CreateObject();

        if (!arrived)
            MovePlayer(); 
    }

    void GetWorldPoint()
    {
        // clear path when selecting a new world position
        path.Clear();

        // shoot a ray from your screen to the world
        RaycastHit hit;
        Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);

        // if the ray hits something, set the target we want to move to
        if (Physics.Raycast(ray, out hit))
        {
            targetPosition = new Vector3(hit.point.x, trans.position.y, hit.point.z);

            // call A* to get optimal path (returns a list of nodes to traverse) (returns correct list of Vector3 coordinates)
            path = pathfinder.AStar(trans.position, targetPosition);

            // get next destination if path is not null
            if (path != null)
            {
                // show all nodes needed to be traversed to reach goal in console
                pathfinder.ShowNodes(path);

                targetPosition = path.Pop();
                arrived = false;
                Debug.Log("Leaders 1st destination: " + targetPosition + ", Remaining path length: " + path.Count);
            }
        }
    }
    void CreateObject()
    {
        // shoot a ray from your screen to the world
        RaycastHit hit;
        Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);

        // if the ray hits something, set the target we want to create an object at
        if (Physics.Raycast(ray, out hit))
        {
            // get the obstacles creation position and floor the position to set it in the middle of the node
            obstaclePosition = new Vector3(((int) hit.point.x) + 0.5f, 1, ((int) hit.point.z) + 0.5f);

            // create a new primitive object and set the position to the ray cast point
            GameObject cylinder = GameObject.CreatePrimitive(PrimitiveType.Cylinder);
            cylinder.transform.position = obstaclePosition;
            cylinder.layer = 0;
            cylinder.transform.localScale = new Vector3(.5f, 1f, .5f);
        }
    }
    void MovePlayer()
    {
        // if path is empty, do nothing
        if (path.Count <= 0 || path == null)
        {
            rb.velocity = Vector3.zero;
            rb.angularVelocity = Vector3.zero;
            arrived = true;
        }

        // get direction vector based on the targetPosition vector
        Vector3 direction = targetPosition - trans.position;

        // rotate player
        targetRotation = Quaternion.LookRotation(-direction); // putting just "direction" here caused it to move opposite
        trans.rotation = Quaternion.Lerp(trans.rotation, targetRotation, Time.deltaTime * movementSpeed);

        // go towards current destination
        rb.velocity = direction * 0.5f;

        // if we are inside the radius of satisfaction, then change destinations
        if (direction.magnitude < radiusOfSat)
        {
            targetPosition = path.Pop();
            Debug.Log("Leaders next destination: " + targetPosition + ", Remaining path length: " + path.Count);
        }
    }
    private void OnCollisionStay(Collision collision)
    {
        if (collision.gameObject.name.Equals("Cylinder"))
        {
            Vector3 toObstacle = collision.gameObject.transform.position - trans.position;
            toObstacle.Normalize();
            toObstacle.y = 0f;

            float dot = Vector3.Dot(trans.right, toObstacle);

            // just rotate around the damn cylinder (orbit it until you ain't touching it I guess)
            if (dot < 0f) // If I'm closer to the left side, go left
            {
                // Debug.Log("I'm on the right side.");
                transform.RotateAround(collision.gameObject.transform.position, new Vector3(0f, 1f, 0f), obstacleDodgeSpeed);
            }
            else // If I'm closer to the right side, go right
            {
                // Debug.Log("I'm on the left side.");
                transform.RotateAround(collision.gameObject.transform.position, new Vector3(0f, -1f, 0f), obstacleDodgeSpeed);
            }

            //Debug.Log("Velocity: " + rb.velocity);
        }
    }
}
