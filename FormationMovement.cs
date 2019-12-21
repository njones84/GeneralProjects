using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FormationMovement : MonoBehaviour
{

    [SerializeField] private Transform formationLeader;
    [SerializeField] private Transform trans;
    [SerializeField] private Rigidbody rb;

    [SerializeField] private float formationDistanceOffset;
    [SerializeField] private float formationRotationOffset;

    private float movementSpeed;
    private float obstacleDodgeSpeed;
    private float radiusOfSat;

    private Vector3 formationPosition;
    private Vector3 targetPosition;
    private Quaternion targetRotation;
    private bool getIntoFormation;

    private void Start()
    {
        movementSpeed = 5f;
        obstacleDodgeSpeed = 1f;
        radiusOfSat = .5f;
        getIntoFormation = false;
    }

    private void Update()
    {
        if (Input.GetMouseButtonDown(0))
            getIntoFormation = true;

        GetPlayerPosition();

        if (getIntoFormation)
            MoveFormationPiece();      
    }

    void MoveFormationPiece()
    {
        // get direction vector based on the formationPosition vector
        Vector3 direction = formationPosition - trans.position;
        direction = new Vector3(direction.x, 0f, direction.z);

        // rotate player
        targetRotation = Quaternion.LookRotation(-direction); // putting just "direction" here caused it to move opposite
        trans.rotation = Quaternion.Lerp(trans.rotation, targetRotation, Time.deltaTime * movementSpeed);

        // if we are outside of the radius of satisfaction, continue moving towards the destination
        if (Vector3.Distance(trans.position, formationPosition) > radiusOfSat)
        {

            rb.velocity = direction;
            Debug.DrawLine(trans.position, formationPosition, Color.red);

        }
        else if (!trans.rotation.Equals(targetRotation)) // if the current rotation is not equal to our target rotation then keep rotating
        {
            trans.rotation = Quaternion.Lerp(trans.rotation, targetRotation, Time.deltaTime * movementSpeed);
        }
        else // stop moving
        {
            rb.velocity = Vector3.zero;
            rb.angularVelocity = Vector3.zero;
        }
    }
    void GetPlayerPosition()
    {
        // Project point forward from leader's forward facing vector
        Vector3 projectedPoint = formationLeader.forward * formationDistanceOffset;
        //Debug.Log("Projected point: " + projectedPoint);

        // Rotate that point to find this character's spot in the formation
        formationPosition = Quaternion.Euler(0f, formationRotationOffset, 0f) * projectedPoint;
        //Debug.Log("Formation position: " + formationPosition);

        formationPosition += formationLeader.position;
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
