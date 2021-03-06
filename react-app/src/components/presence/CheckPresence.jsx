import React, { Component } from "react";
import { withRouter } from "react-router";
import Axios from "axios";
import Select from "react-select";

class CheckPresence extends Component {
  state = {
    presenceDate: "",
    classroom: { childList: [] },
    presenceListEntries: [],

    alertType: "",
    alertMessage: ""
  };
  render() {
    return (
      <div className="container">
        <div className="row">
          <button
            className="btn btn-primary m-2"
            onClick={e =>
              this.props.history.push(
                "/classroom/details/" + this.props.match.params.classId
              )
            }
          >
            Return
          </button>
          <button
            className="btn btn-success m-2"
            onClick={e => this.savePresence()}
          >
            Save
          </button>
          <div className="m-2 col-md-3">
            <input
              type="date"
              disabled
              className="form-control"
              value={this.state.presenceDate}
              onChange={e => {
                this.setState({ presenceDate: e.target.value });
              }}
            />
          </div>
        </div>

        <h1>{Date.getDate}</h1>
        <table className="table table-striped table-dark table-hover table-sm">
          <thead className="thead-dark">
            <tr>
              <th scope="col">#</th>
              <th scope="col">Last name</th>
              <th scope="col">First name</th>
              <th scope="col" />
            </tr>
          </thead>
          <tbody>
            {this.state.classroom.childList.map(child => (
              <tr>
                <td>{this.state.classroom.childList.indexOf(child) + 1}</td>
                <td>{child.surname}</td>
                <td>{child.name}</td>
                <Select
                  options={[
                    { value: "ABSENT", label: "absent" },
                    { value: "PRESENT", label: "present" },
                    { value: "LATE", label: "late" }
                  ]}
                  onChange={e => this.changePresence(child, e.value)}
                />
              </tr>
            ))}
          </tbody>
        </table>
        <div className="row">
          <div className={"m-2 alert alert-" + this.state.alertType}>
            {this.state.alertMessage}
          </div>
        </div>
      </div>
    );
  }

  changePresence = (child, presenceValue) => {
    let obj = {
      presenceDate: this.state.presenceDate,
      presence: presenceValue,
      child: child
    };

    let presenceList = this.state.presenceListEntries.filter(
      preseceElement => preseceElement.child.id !== child.id
    );

    presenceList.push(obj);

    this.setState({ presenceListEntries: presenceList });
  };

  savePresence = () => {
    let requestData = {
      classId: this.props.match.params.classId,
      loginData: this.props.session,
      presenceListEntries: this.state.presenceListEntries,
      presenceListEntry: this.state.presenceListEntries[0],
      date: this.state.presenceDate
    };

    Axios.post(this.props.apiHost + "/presence/add_list", requestData)
      .then((res, req) =>
        this.setState({ alertType: "success", alertMessage: res.data.message })
      )
      .catch(err =>
        this.setState({
          alertType: "success",
          alertMessage:
            "An error ocurred, please try again or contat with admin"
        })
      );
  };

  componentDidMount() {
    let presenceDate = new Date().toISOString().slice(0, 10);
    this.setState({ presenceDate });

    const requestData = {
      loginData: this.props.session,
      classId: this.props.match.params.classId
    };

    Axios.post(this.props.apiHost + "/class/profile", requestData)
      .then((res, req) => {
        this.setState({ classroom: res.data });
      })
      .catch(err =>
        this.setState({
          alertType: "warning",
          alertMessage: "Problem with contact to the server"
        })
      );
  }
}

export default withRouter(CheckPresence);
